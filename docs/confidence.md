# Rating and score confidence

A feature value may be unknown. A score should expect an unknown value of a feature,
and still produce a score value.
In such a case, the score has to produce a result taking into account some amount of uncertainty.
The same applies to a rating.

## Confidence level

To let a user know about how accurate a score value is, a score provides a confidence level
for the calculated score value.

Let's define a **confidence level** as a float number in the interval `[0, 10]`
where `0` means the lowest confidence, and `10` means the highest confidence.

Both a score and a rating provide a confidence level for score and rating values
that they produce. The confidence level mainly depends on a number of unknown feature values
that were used to calculate a score or rating value.

The way how a confidence level is calculated may depend on a particular score.
A score should also take into account the weights of the sub-scores during calculating a score value.

---

Next: [Open-source security rating](oss_security_rating.md)
