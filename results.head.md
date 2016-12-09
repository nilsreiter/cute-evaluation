# CUTE -- CRETA Unshared Task on Entity References

## Evaluation Results (track 1)

### Setup

**Strict evaluation**: A system entity is counted as correct if and only if both the span and the category are equal to an entity in the gold standard.

**Precision and Recall**: https://en.wikipedia.org/wiki/Precision_and_recall

### Systems

**BL-NER**: Baseline consisting of applying the newest standard model for German NER, using the Stanford Named Entity Recognizer.

**DFKI**: CUTE participant

**IMS**: Das System ist ein Neuronales Netz, das sequence labelling-fähig ist.
Anstatt Features aus Wörtern und Texten mithilfe des Einsatzes externer
Tools zu extrahieren, lernt es textintern sowohl Wort- als auch
Charakterebenenfeatures. Das macht es zwar besonders leicht übertragbar,
führt aber dazu, dass es Unemengen an Trainingsdaten braucht, um
implizit Information wie Struktur etc. zu lernen. Das zeigt sich auch an
den Ergebnissen.

**IMS2**: Conditional random field. Features: wordform, (NHG) pos tag, character category patterns[^1]. 2 preceding and 1 following token.


[^1]: http://cleartk.github.io/cleartk/apidocs/2.0.0/org/cleartk/ml/feature/function/CharacterCategoryPatternFunction.PatternType.html#REPEATS_MERGED
