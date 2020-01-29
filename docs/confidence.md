# Rating confidence

A feature value may be unknown. A rating should expect an unknown value for a feature,
and still produce a score value with a label.
In such a case, the rating has to produce a result taking into account some amount of uncertainty.

## Confidence level

To let a user know about how accurate the rating value is, the rating provides a confidence level
for a calculated rating value.

Let's define the **confidence level** is a float number in the interval `[0, 10]`
where `0` means the lowest confidence, and `10` means the highest confidence.

Both a score and a rating provide a confidence level for score and rating values
that they produce. The confidence level mainly depends on a number of unknown feature values
which were used to calculate a score or rating value.

## Confidence level for a score

Calculation of a confidence level may depend on a particular score.

## Confidence level for a rating

A rating should also take into account the weights of the scores during calculating a rating value.

---

Next: [Open-source security rating](oss_security_rating.md)
