# CUTE -- CRETA Unshared Task on Entity References

## Evaluation Results (track 1)

### Setup

**Strict evaluation**: A system entity is counted as correct if and only if both the span and the category are equal to an entity in the gold standard.

**Loose evaluation**: As long as there is a single token  overlap between system and gold we count it as a true positive.

**Precision and Recall**: https://en.wikipedia.org/wiki/Precision_and_recall

### Systems

**BL-NER**: Baseline consisting of applying the newest standard model for German NER, using the Stanford Named Entity Recognizer.

**DFKI**: CUTE participant

**IMS2**: Conditional random field. Features: wordform, (NHG) pos tag, character category patterns[^1]. 2 preceding and 1 following token. Additional features:

- **+case**:  Case lookup. We check whether an upper-case token also appears in lower case
- **+mhd pos**: New middle high German part of speech tags
- **+names list**: A list of names extracted from Parzival, Erec, Willehalm and Iwein.

[^1]: http://cleartk.github.io/cleartk/apidocs/2.0.0/org/cleartk/ml/feature/function/CharacterCategoryPatternFunction.PatternType.html#REPEATS_MERGED
