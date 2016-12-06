#!/usr/bin/perl

use warnings;

my $DIR="/Users/reiterns/Documents/CRETA/cute/results";
my @corpora = ("bundestagsdebatten", "adorno", "parzival");
my @systems = ("dfki", "ims", "ims2", "baseline-ner");

my $tag = shift;
my $column = shift; #2; # 2 precision, 3 recall

printf("%20s\t","corpus");
print join("\t", @systems)."\n";

for my $c (@corpora) {
  printf("%20s\t", $c);
  for my $system (@systems) {
    open(FH, "cat $DIR/$system/$c/$c.txt | grep $tag |");
    while(<FH>) {
      chomp;
      my @l = split;
      printf("%5.2f\t", $l[$column]*100);
    }
    close FH;
  }
  print "\n";
}
