#!/bin/bash

GOLD=/Users/reiterns/Documents/CRETA/cute/gold/xmi
SILVER_BASE_DIR=/Users/reiterns/Documents/CRETA/cute/silver
OUTPUT_BASE_DIR=/Users/reiterns/Documents/CRETA/cute/results

JAR_FILE=target/creta.cute.evaluation-0.0.1-SNAPSHOT.jar


#### DFKI

LABEL=dfki
C=parzival
CLASSES="PER LOC"
java -jar $JAR_FILE --gold $GOLD --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=adorno
CLASSES="PER WRK"
java -jar $JAR_FILE --gold $GOLD --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=bundestagsdebatten
CLASSES="PER LOC ORG WRK"
java -jar $JAR_FILE --gold $GOLD --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

#### Sarah

LABEL=ims
C=parzival
CLASSES="PER LOC ORG WRK EVT CNC"
java -jar $JAR_FILE --gold $GOLD --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=adorno
CLASSES="PER WRK ORG EVT CNC LOC"
java -jar $JAR_FILE --gold $GOLD --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=bundestagsdebatten
CLASSES="PER WRK ORG EVT CNC LOC"
java -jar $JAR_FILE --gold $GOLD --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

#### Nils

LABEL=ims2
C=parzival
CLASSES="PER LOC ORG WRK EVT CNC"
java -jar $JAR_FILE --gold $GOLD --format xmi --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=adorno
CLASSES="PER LOC ORG WRK EVT CNC"
java -jar $JAR_FILE --gold $GOLD --format xmi --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=bundestagsdebatten
CLASSES="PER LOC ORG WRK EVT CNC"
java -jar $JAR_FILE --gold $GOLD --format xmi --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES



#### BASELINE

LABEL=baseline-ner
C=parzival
CLASSES="PER LOC ORG WRK EVT CNC"
java -jar $JAR_FILE --gold $GOLD --format xmi --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=adorno
CLASSES="PER WRK ORG EVT CNC LOC"
java -jar $JAR_FILE --gold $GOLD --format xmi --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES

C=bundestagsdebatten
CLASSES="PER WRK ORG EVT CNC LOC"
java -jar $JAR_FILE --gold $GOLD --format xmi --label $LABEL_$C --silver $SILVER_BASE_DIR/$LABEL/$C --output $OUTPUT_BASE_DIR/$LABEL/$C --classes $CLASSES
