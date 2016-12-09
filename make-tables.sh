#!/bin/bash

CLASSES="PER LOC ORG WRK CNC"

SCORES="1 2"
TGT="results.md"

cat results.head.md > results.md

echo "" >> $TGT
for C in $CLASSES
do
  echo "### $C" >> $TGT
  echo "" >> $TGT
  echo "" >> $TGT
  perl make-table.pl $C 1 >> $TGT
  echo "" >>$TGT
  echo "Table: Precision" >> $TGT
  echo "" >> $TGT
  echo "" >> $TGT
  perl make-table.pl $C 2 >> $TGT
  echo "" >> $TGT
  echo "Table: Recall" >> $TGT
  echo "" >> $TGT
done


pandoc -o results.pdf results.md
