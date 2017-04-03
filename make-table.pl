#!/usr/bin/perl

use warnings;

my $DIR="/Users/reiterns/Documents/CRETA/cute/results";
my @corpora = (#"bundestagsdebatten", "adorno",
  "parzival"#,
  #"werther"
);
my %systems = ("dfki"=>"dfki",
    #"ims"=>"ims",
    "baseline-ner"=>"bl-ner",
    "ims2"=>"ims2",
    "ims2+case"=>"+case",
    "ims2+case+tt"=>"+mhd pos",
    "ims2-1.3.0"=>"+names list");

my $tag = shift;
my $column = shift; #2; # 2 precision, 3 recall
my $evaltype = shift; # strict loose

printf("| %20s | ","corpus");
for my $s (sort keys %systems) {
  printf("%8s | ", $systems{$s});
}
print "\n";
printf("| --------------------:| ", "");
for my $s (sort keys %systems) {
  printf("%9s| ", "--------:");
}
print"\n";
#print join(" | ", @systems)." |\n";


for my $c (@corpora) {
  printf("| %20s | ", $c);
  for my $system (sort keys %systems) {
    if (-e "$DIR/$system/$c/$c.$evaltype.txt") {
      open(FH, "cat $DIR/$system/$c/$c.$evaltype.txt | grep $tag |");
      while(<FH>) {
        chomp;
        my @l = split;
        printf("%8.2f | ", $l[$column]*100);
      }
      close FH;
    } else {
      open(FH, "cat $DIR/baseline-ner/$c/$c.$evaltype.txt | grep $tag |");
      while(<FH>) {
        printf("%8s | ", "--");
      }
      close FH;
    }
  }
  print "\n";
}
