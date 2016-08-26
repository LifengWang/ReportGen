#!/usr/bin/python
import pandas as pd
import sys


def main():
    # fle_path = '/home/alex/workspace/ReportGen/python/BigBenchTimes.csv'
    file_path = sys.argv[1]
    df = pd.read_csv(file_path, sep=';')
    df.to_excel("BigBenchTimes.xlsx")
    print df.to_string()


if __name__ == '__main__':
    main()
