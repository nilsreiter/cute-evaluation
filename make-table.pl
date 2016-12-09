#!/usr/bin/perl

use warnings;

my $DIR="/Users/reiterns/Documents/CRETA/cute/results";
my @corpora = ("bundestagsdebatten", "adorno", "parzival");
my %systems = ("dfki"=>"dfki",
    "ims"=>"ims",
    "ims2"=>"ims2",
    "baseline-ner"=>"bl-ner");

my $tag = shift;
my $column = shift; #2; # 2 precision, 3 recall

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
    open(FH, "cat $DIR/$system/$c/$c.txt | grep $tag |");
    while(<FH>) {
      chomp;
      my @l = split;
      printf("%8.2f | ", $l[$column]*100);
    }
    close FH;
  }
  print "\n";
}
