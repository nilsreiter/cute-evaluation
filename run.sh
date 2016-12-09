#!/bin/bash

GOLD=/Users/reiterns/Documents/CRETA/cute/gold/xmi
SILVER_BASE_DIR=/Users/reiterns/Documents/CRETA/cute/silver
OUTPUT_BASE_DIR=/Users/reiterns/Documents/CRETA/cute/results

JAR_FILE=target/creta.cute.evaluation-0.0.1-SNAPSHOT.jar


#### DFKI


LABEL=dfki

echo "Evaluating $LABEL ..."

C=parzival
CLASSES="PER LOC"
echo "  $C"
java -jar $JAR_FILE --gold $GOLD --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=adorno
CLASSES="PER WRK"
echo "  $C"
java -jar $JAR_FILE --gold $GOLD --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=bundestagsdebatten
CLASSES="PER LOC ORG WRK"
echo "  $C"
java -jar $JAR_FILE --gold $GOLD --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=werther
CLASSES="PER LOC ORG WRK"
echo "  $C"
java -jar $JAR_FILE --gold $GOLD --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES


#### Sarah

LABEL=ims

echo "Evaluating $LABEL ..."

C=parzival
CLASSES="PER LOC ORG WRK EVT CNC"
echo "  $C"
java -jar $JAR_FILE --gold $GOLD --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=adorno
CLASSES="PER WRK ORG EVT CNC LOC"
echo "  $C"
java -jar $JAR_FILE --gold $GOLD --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=bundestagsdebatten
CLASSES="PER WRK ORG EVT CNC LOC"
echo "  $C"
java -jar $JAR_FILE --gold $GOLD --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=werther
CLASSES="PER WRK ORG EVT CNC LOC"
echo "  $C"
java -jar $JAR_FILE --gold $GOLD --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES


#### Nils

LABEL=ims2

echo "Evaluating $LABEL ..."

C=parzival
CLASSES="PER LOC ORG WRK EVT CNC"
echo "  $C"
java -jar $JAR_FILE --gold $GOLD --format xmi --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=adorno
CLASSES="PER LOC ORG WRK EVT CNC"
echo "  $C"
java -jar $JAR_FILE --gold $GOLD --format xmi --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=bundestagsdebatten
CLASSES="PER LOC ORG WRK EVT CNC"
echo "  $C"
java -jar $JAR_FILE --gold $GOLD --format xmi --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=werther
CLASSES="PER LOC ORG WRK EVT CNC"
echo "  $C"
java -jar $JAR_FILE --gold $GOLD --format xmi --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES


#### BASELINE

LABEL=baseline-ner

echo "Evaluating $LABEL ..."

C=parzival
CLASSES="PER LOC ORG WRK EVT CNC"
echo "  $C"
java -jar $JAR_FILE --gold $GOLD --format xmi --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=adorno
CLASSES="PER WRK ORG EVT CNC LOC"
echo "  $C"
java -jar $JAR_FILE --gold $GOLD --format xmi --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=bundestagsdebatten
CLASSES="PER WRK ORG EVT CNC LOC"
echo "  $C"
java -jar $JAR_FILE --gold $GOLD --format xmi --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=werther
CLASSES="PER WRK ORG EVT CNC LOC"
echo "  $C"
java -jar $JAR_FILE --gold $GOLD --format xmi --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES
