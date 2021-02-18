# Quality assurance

This page defines quality requirements and a verification procedure 
for [scoring functions and rating procedures](ratings.md).
The purpose of a verification procedure is to make sure that a defined rating procedure
produces expected and meaningful results.

## Test vectors

Tests vectors may be defined for:

*  A rating procedure.
*  A scoring function that consumes features (feature-based scoring function).
*  A scoring function that consumes other scores (score-based scoring function).

Given `N` features `f[i]`, let's define a **test vector for a feature-based scoring function**
as a pair `(v, e)` of the following elements:

*  `v` is a vector `(v[1], ... , v[N])` where `v[i]` contains a value of feature `f[i]`.
*  `e` is an interval `[a, b]` of an expected score
    where `0 <= a <= b <= 10` and `abs(a - b) != 0`.

Given `N` scores `s[i]`, let's define a **test vector for a score-based scoring function**
as a pair `(v, e)` of the following elements:

*  `v` is a vector `(v[1], ... , v[N])` where `v[i]` contains a score `s[i]`.
*  `e` is an interval `[a, b]` of an expected score
    where `0 <= a <= b <= 10` and `abs(a - b) != 0`.

## Building test vectors

A set of test vectors defines **quality requirements** for a rating procedure or scoring function.

There are two main strategies for defining test vectors:

1.  By using real open source projects. This way, we take an existing project, collect data about the project
    to build the vector `v`, and then define the interval `e`.
1.  By using hypothetical open source projects. This way, we define both the vector `v` and the interval `e`.

The strategies above may be combined.

## Test vectors based on real open source projects

Define `K` test vectors `real_test_vector[j]` where `j = 1..K`:

1.  Select `K` existing open source projects `real_oss_project[j]` where `j = 1..K`.
1.  For each `real_oss_project[j]` where `j = 1..K`:
    1.  Build a vector of feature values `v`: for each feature `f[i]`, gather the value `v[i]` where `i = 1..N`.
    1.  Based on your knowledge of the project and your expertise, define an interval `e` for the vector `v`.
    1.  Assign a test vector `real_test_vector[j] = (v, e)`.

Notes:

*  It's good to select open source projects that have diverse feature values.
*  It's good if the interval `e` from test vectors cover the whole `[0, 10]` interval.

   In other words, `union(real_test_vector[j].e)` should be close to `[0, 10]` where `j = 1..K`.
*  It's good if values `e` are provided by experts in the domain and by those who have good knowledge about the project.

## Test vectors based on hypothetical open source projects

Define `L` test vectors `abstract_test_vector[j]` where `j = 1..L`:

1.  Define a vector of feature values `v`: for each feature `f[i]`, define `v[i]` where `i = 1..N`.
1.  Based on your expertise, define an interval `e`, define an interval `e` for the vector `v`.
1.  Assign a test vector `abstract_test_vector[j] = (v, e)`.

Notes:

*  It's good if the intervals `e` from test vectors cover the whole `[0, 10]` interval.

   In other words, `union(abstract_test_vector[j].e)` should be closed to `[0, 10]` where `j = 1..L`.
*  It's good if values `e` are provided by experts in the domain.

## Verification procedure

Rating procedures and scoring functions must pass all tests defined by test vectors.

Let's say we have a rating procedure `rating(v)`.
The procedure takes a vector `v` that contains values of features, and returns a score.

Then, the following verification procedure can be applied to make sure that the procedure `rating(v)`
behaves as expected:

```
verify(test_vectors, rating) {
    for each test_vector from test_vectors {
        actual_rating = rating(test_vector.v)
        if actual_rating does not belong to test_vector.e {
            return Failed
        }
    }
    return Passed
}
```

This procedure may be also applied to a scoring function.

## Implementation

The verification procedure defined above is implemented in
[com.sap.oss.phosphor.fosstars.model.qa](https://github.com/SAP/fosstars-rating-core/tree/master/src/main/java/com/sap/oss/phosphor/fosstars/model/qa) package.

---

Next: [Tuning](tuning.md)
