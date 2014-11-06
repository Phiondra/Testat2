#!/bin/bash
for i in C D E F G H I J K L M N O P Q R S T U V W ; do
  echo $i $(wc -l LargeCatalogue.txt)
  egrep "^[AB]" LargeCatalogue.txt | sed  "s/^[AB]/$i/" > /tmp/new_catalogue_lines
  cat /tmp/new_catalogue_lines >> LargeCatalogue.txt
done
