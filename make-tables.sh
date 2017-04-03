#!/bin/bash

CLASSES="PER" # LOC ORG WRK CNC"

SCORES="1 2"
TGT="results.md"
TYPES="strict loose"


cat results.head.md > $TGT

echo "" >> $TGT
for T in $TYPES
do
  echo "### $T" >> $TGT
  for C in $CLASSES
  do
    echo "#### $C" >> $TGT
    echo "" >> $TGT
    echo "" >> $TGT
    perl make-table.pl $C 1 $T >> $TGT
    echo "" >>$TGT
    echo "Table: Precision" >> $TGT
    echo "" >> $TGT
    echo "" >> $TGT
    perl make-table.pl $C 2 $T >> $TGT
    echo "" >> $TGT
    echo "Table: Recall" >> $TGT
    echo "" >> $TGT
  done
done


pandoc -o results.pdf $TGT
