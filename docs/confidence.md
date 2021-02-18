# Confidence of scores and ratings

A feature value may be unknown. In turn, scoring functions should expect unknown values,
and still produce a score. In this case, the scoring function has to produce a result 
taking into account some amount of uncertainty. The same applies to rating procedures.

To let a user know about how accurate a score is, a scoring function provides a confidence level
for the calculated score.

Let's define a **confidence level** as a float number in the interval `[0, 10]`
where `0` means the lowest confidence, and `10` means the highest confidence.

Both scoring function and rating procedure provide a confidence level for score and rating values
that they produce. The confidence level mainly depends on a number of unknown features
that were used to calculate the score.

The way how a confidence level is calculated depends on a particular scoring function.
In general, a scoring function should take into account the weights of the sub-scores.

---

Next: [open source security rating](oss_security_rating.md)
