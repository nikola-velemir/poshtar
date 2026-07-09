#!/bin/sh
rm *crop*.pdf
for FILE in *.pdf; do
  pdfcrop "${FILE}"
done
