#!/bin/bash
#Create single node collect script
if [ ! -f collect.sh ]; then
cat > collect.sh << 'EOF'
#!/bin/bash
#This script is used to check the server
#system info
system_info() {
echo "   <system-release>`cat /etc/redhat-release`</system-release>"
echo "   <kernel-release>`uname -a|awk '{print $1,$3}'`</kernel-release>"
echo "   <server-model>`dmidecode | grep "Product Name:"|sed -n '1p'|awk -F': ' '{print $2}'`</server-model>"
echo "   <hostname>`hostname`</hostname>"
echo
}
#CPU info
cpu_info() {
echo "    <frequency>`cat /proc/cpuinfo | grep "model name" | uniq |awk -F': ' '{print $2}'`</frequency>"
echo "    <physical-count>`cat /proc/cpuinfo | grep "physical id" | sort -u| wc -l`</physical-count>"
echo "    <logic-cores>`cat /proc/cpuinfo | grep "processor" | sort -u| wc -l `</logic-cores>"
echo "    <cache-size>`cat /proc/cpuinfo| grep "cache size"|uniq|awk '{print $4}'`</cache-size>"
echo
}
#memory info
mem_info() {
#memory=`dmidecode |grep "Range Size"|head -1|awk '{print $3}'`
memory=`dmidecode |grep "Size"|grep MB|awk '{print $2}'|awk '{sum+=$1}END{print sum}'`
echo "   <memory>${memory}</memory>"
}
#disk and partitions
swap_pos=`cat /proc/swaps|sed -n '2p'|awk '{print $1}'`
partition_info() {
echo "`fdisk -l|grep Disk|awk -F, '!/identifier/ {print $1}' | awk -F '/|:| ' '{print "<"$4">"$6"</"$4">"}'`"
echo "<disk_number>`fdisk -l|grep "Disk /dev/sd" |wc -l`</disk_number>"
echo "<disk_total>`fdisk -l|grep Disk|awk -F, '!/identifier/ {print $1}' | awk -F '/|:| ' '{print ""$6""}' |awk '{a+=$1}END{print a}'`</disk_total>"
#echo
}
#network adapter info
adapter_info() {
echo "    <eth_number>`ifconfig -a |grep eth |wc -l`</eth_number>"
n=0
for i in `ifconfig -a |grep eth |awk '{print $1}'`; do
ret=`ifconfig $i 2>/dev/null`
if [ 0 = $? ]; then
port=$i
ip=`echo $ret | sed -n 's#.*addr:\(.*\)Bcast.*#\1#gp'`
speed=`ethtool $i|grep Speed|awk '{print $2}' | sed 's#\(.*\)Mb/s#\1#g' 2>/dev/null`
if [ $? = 0 ]; then
echo "    <eth${n}_name>${port}</eth${n}_name>"
echo "    <eth${n}_ip>${ip}</eth${n}_ip>"
echo "    <eth${n}_speed>${speed}</eth${n}_speed>"
((n++))
fi
fi
done
echo
}
#software package
software_info() {
echo "**********************************************"
echo "SELinux is `cat /etc/selinux/config |grep SELINUX=disabled|awk -F= '{print $2}'||echo "enabled"`"
echo "`service iptables status|sed 's/Firewall/Iptables/g'`"
echo
echo "**********************************************"
sed -n '/%packages/,/%post/p;' /root/anaconda-ks.cfg|sed '/%post/d;/^$/d'
echo "**********************************************"
}
#del mac-addr
#sed -i '/HWADDR/d' /etc/sysconfig/network-scripts/ifcfg-eth0
#sed -i '/HWADDR/d' /etc/sysconfig/network-scripts/ifcfg-eth1
system_info
cpu_info
mem_info
partition_info
adapter_info
#cluster size
#echo "<cluster size>`cat /etc/hosts |grep node |wc -l`</cluster size>"
#platform
#p1 = echo "`cat /proc/cpuinfo |grep "model name" |uniq` "
if cat /proc/cpuinfo |grep "model name" |uniq |grep -q "v3"
then echo "<platform>Haswell</platform>"
else if cat /proc/cpuinfo |grep "model name" |uniq |grep -q "v4"
then echo "<platform>Broadwell</platform>"
fi
fi
#Network
echo "<network>`ifconfig |grep RUNNING |sed -n "1, 1p" |awk '{print $1}' |xargs ethtool | grep Speed |awk '{print $2}'`</network>"
#JDK
#echo "<jdk>`echo $JAVA_HOME`</jdk>"
java -version 2>java_v.txt
echo "<jdk>`cat java_v.txt | grep version | awk -F '"' '{print $2}'`</jdk>"
#python
echo "<python>`python -V 2>&1 | awk '{print $2}'`</python>"
#Hadoop/YARN/HDFS
echo "<Hadoop/YARN/HDFS>`find / -name hadoop-common* |grep SNAPSHOT.jar |grep -v lib |awk -F '-' '{print $5}'`</Hadoop/YARN/HDFS>"
#hive
echo "<hive>`find / -name hive-common-* |grep parcels |grep jars |awk -F '-' '{print $5}'`</hive>"
#spark
echo "<spark>`find / -name spark-assembly-*.jar |grep cloudera |grep jars | awk -F '-' '{print $5}'`</spark>"
#BigBench
#CDH
echo "<CDH>`cat /etc/yum.repos.d/cm.repo |grep baseurl |awk -F '/' '{print $11}'`</CDH>"
EOF
fi
if [ 0 = $# ]; then
  echo "Usage : sh collect_all.sh HOST[x-y] HOSTz" 
  echo "e.g. sh collect_all.sh bb-node[1-8] OR sh collect_all.sh bb-node[1-4] bb-node[5-5] bb-node[6-8]"
  exit
fi
rm -rf info.xml
#Config SSH login without password
echo "Config SSH login without password..."
if [ ! -f /root/.ssh/id_rsa.pub ]; then
  ssh-keygen -t rsa
fi
echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" >>info.xml
echo "<node-list>" >>info.xml
for x in $@; do
if [[ "$x" =~ .*\[.*\] ]]; then
prefix=`echo $x | sed -n 's#\(^.*\)\[[0-9]\{1,\}-[0-9]\{1,\}\]#\1#gp'`
start=`echo $x | sed -n 's#^.*\[\([0-9]\{1,\}\)-[0-9]\{1,\}\]$#\1#gp'`
end=`echo $x | sed -n 's#^.*\[[0-9]\{1,\}-\([0-9]\{1,\}\)\]$#\1#gp'`
for((i=$start; i<=$end; i++));do
  scp /root/.ssh/id_rsa.pub ${prefix}$i:/root/.ssh/authorized_keys > /dev/null 2>&1
  echo "SCP scripts to node ${prefix}$i"
  scp collect.sh ${prefix}$i:/root/ > /dev/null 2>&1
  echo "Install dmidecode on ${prefix}$i..."
  ssh ${prefix}$i "yum -y install dmidecode" > /dev/null 2>&1
  echo "Collect cluster configuration of ${prefix}$i"
  echo "<node>" >>info.xml
  ssh ${prefix}$i "sh /root/collect.sh" >>info.xml
  echo "</node>" >>info.xml
done
fi
done;
echo "</node-list>" >> info.xml
echo "The hardware result is stored in [info.xml], please upload this file to analysis system."
rm -f collect.sh
grep -v '^$' info.xml > info_end.xml
