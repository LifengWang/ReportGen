Copyright (c) 2016, Intel Corporation.


This is a auto-report tool for BigBench.




======

This document is to describe the installation for auto-report tool and usage.

# Preparation

## Cluster Environment

**Cloudera**

* Developed and tested on Cloudera CDH 5.5.1


**Java**

Java 1.8 is required. 64 bit is recommended. A suitable JDK is installed along with Cloudera (if using the parcel installation method).


**Other necessary system packages**

* bash
* sed
* awk
* grep
* tar
* zip

## Installation

On the SUT, clone the github repository into a folder stored in $PROJECT_DIR:

```
export PROJECT_DIR="$HOME"     # adapt this to your location
cd "$PROJECT_DIR"
git clone https://github.com/LifengWang/ReportGen
cd "$PROJECT_DIR/scripts"
sh collect.sh <hosts of your cluster>               #e.g. sh collect.sh hsw-node[1-2] hsw-node[4-5] hsw-node[7-7] hsw-node[10-12]
```

## Configuration

Copy a latest yarn-site.xml from any datanode (/var/run/cloudera-scm-agent/process/${id}-yarn-NODEMANAGER/yarn-site.xml) to your Project folder in master node.


`ssh $DATANODE`

Find out a yarn-NODEMANAGER folder  a latest yarn-site.xml exsits:

`ls -t /var/run/cloudera-scm-agent/process/ |grep yarn-NODEMANAGER |sed -n '1p'`

`scp /var/run/cloudera-scm-agent/process/${id}-yarn-NODEMANAGER/yarn-site.xml root@MASTERNODE:$PROJECT_DIR`

# Run
Package your project before executing the command below.

If you want to generate BigBench report and excel file of elapse time for each query and phrase, choose 'both' as $GENERATION_TYPE.

If you only want to generate BigBench report or excel file of elpase time, choose 'doc' or 'xls' as $GENERATION_TYPE. 


Running command:

```
cd $PROJECT_DIR
mvn clean package
java -jar target/ReportGen-1.0-SNAPSHOT.jar "$BIGBENCH_RESULT_ZIPFILE" "$XMLFILE_DIR" "$GENERATION_TYPE"
```
E.g. `java -jar target/ReportGen-1.0-SNAPSHOT.jar /home/bb_result.zip /home/ReportGen/scripts/ doc`

OR `java -jar target/ReportGen-1.0-SNAPSHOT.jar /home/bb_result.zip /home/ReportGen/scripts/ xls`

OR `java -jar target/ReportGen-1.0-SNAPSHOT.jar /home/bb_result.zip /home/ReportGen/scripts/ both`
