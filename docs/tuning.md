# Tuning

A weight for a score defines how important the score is,
and how much the score contributes to a rating.

To produce meaningful and expected results defined by the [quality requirements](qa.md),
rating procedures and scoring functions have to be configured with right weights.

This page describes several ways how the weights can be assigned to meet the quality requirements.

## Manual weights adjustment

Weights can be configured manually based on expert's knowledge and expertise.
It may be relatively easy if there are not too many features, scores and test vectors.
The more complex a rating becomes, the harder it's to configure weights to meet the quality requirements.

## Automated weights adjustment

Let's consider an implementation of a rating procedure as a function `calculate_rating(v, w)`
that takes two parameters:

*  `v` is a vector of values for features `f[i]` where `i = 1..N`.
*  `w` is a vector of weights for scores `s[j]` where `j = 1..M`.

The function `calculate_rating(v, w)` returns a score that is a float number in the interval `[0, 10]`.

A set of [test vectors](qa.md) `t[k]` defines constrains `t[k].e.a <= calculate_rating(t[k].v, w) <= t[k].e.b`
where `k = 1..L`.

Let's define a function `fit_expected_interval(r, e)` that shows how well `r` fits to the interval `e`.
The function returns a non-negative number. The less the returned values is,
the better the rating `r` fits to the specified interval `e`.

Such a function may be defined as the following:

```
def fit_expected_interval(r, e) {
    mean = e.a + (e.b - e.a) / 2
    return abs(mean - r)
}
```

Next, let's define a function `fit_test_vectors(calculate_rating, t, w)`
that shows how well the `calculate_rating(v, w)` function produces scores with the specified weights `w`.
The function returns a non-negative float number. The less the number is, the better the produced scores are.

Such a function may be defined as the following:

```
fit_test_vectors(calculate_rating, t, w) {
    result = 0
    for each test_vector from t
        rating = calculate_rating(test_vector.v, w)
        result = result + fit_expected_interval(rating, test_vector.e)
    return result
}
```

Then, we need to find such a vector of weights `w*` that minimizes the function `fit_test_vectors(calculate_rating, t, w)`
with the specified function `calculate_rating(v, w)` and constrains defined by the test vectors:

```
0 < w[j] <= 1 , where j = 1..M
0 <= t[k].e.a <= t[k].e.a <= 0 , where k = 1..L
t[k].e.a <= calculate_rating(t[k].v, w) <= t[k].e.b , where k = 1..L

fit_test_vectors(calculate_rating, t, w) -> min
```

There may be multiple ways to find such a vector `w*` that minimizes the function `fit_test_vectors`.
For example:

*  Classic and stochastic gradient descent if the function is differentiable.
*  Genetic algorithms.

## Semi-automated weights adjustment

The two approaches described above may be combined:

1.  Manually assign weights for a subset of most important scores.
1.  Apply the procedure above to find weights for the rest of the scores.

## Implementation

The [com.sap.oss.phosphor.fosstars.model.tuning](https://github.com/SAP/fosstars-rating-core/tree/master/src/main/java/com/sap/oss/phosphor/fosstars/model/tuning)
package contains the optimization procedure described above.
[TuningWithCMAES](https://github.com/SAP/fosstars-rating-core/blob/master/src/main/java/com/sap/oss/phosphor/fosstars/model/tuning/TuningWithCMAES.java)
class uses [CMA-ES algorithm](https://en.wikipedia.org/wiki/CMA-ES).

---

Next: [Rating confidence](confidence.md)
