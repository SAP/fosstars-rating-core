# Quality assurance

This page defines quality requirements and a verification procedure for a [ratings](ratings.md).
The purpose of a verification procedure is to make sure that a defined rating
provides expected and adequate results.

## Test vectors

Given `N` features `f[i]`, let's define a **test vector** as a pair `(v, e)` of the following elements:

*  `v` is a vector `(v[1], ... , v[N])` where `v[i]` contains a value of feature `f[i]`.
*  `e` is an interval `[a, b]` for a rating where `0 <= a <= b <= 10` and `abs(a - b) != 0`.

A set of test vectors defines **quality requirements** for a rating.

## Defining a set of test vectors

There are two main strategies for defining test vectors:

1.  By using real open-source projects. This way, we take an existing project, collect data about the project
    to build the vector `v`, and then define the interval `e`.
1.  By using abstract open-source projects. This way, we define both the vector `v` and the interval `e`.

The strategies above may be combined.

## Test vectors based on real open-source projects

Define `K` test vectors `real_test_vector[j]` where `j = 1..K`:

1.  Select `K` existing open-source projects `real_oss_project[j]` where `j = 1..K`.
1.  For each `real_oss_project[j]` where `j = 1..K`:
    1.  Build a vector of feature values `v`: for each feature `f[i]`, gather the value `v[i]` where `i = 1..N`.
    1.  Based on your knowledge of the project and your expertise, define an interval `e` for the vector `v`.
    1.  Assign a test vector `real_test_vector[j] = (v, e)`.

Notes:

*  It's good to select open-source projects which have diverse feature values.
*  It's good if the interval `e` from test vectors cover the whole `[0, 10]` interval.

   In other words, `union(real_test_vector[j].e)` should be close to `[0, 10]` where `j = 1..K`.
*  It's good if values `e` are provided by experts in the domain and by those who have good knowledge about the project.

## Test vectors based on abstract open-source projects

Define `L` test vectors `abstract_test_vector[j]` where `j = 1..L`:

1.  Define a vector of feature values `v`: for each feature `f[i]`, define `v[i]` where `i = 1..N`.
1.  Based on your expertise, define an interval `e`, define an interval `e` for the vector `v`.
1.  Assign a test vector `abstract_test_vector[j] = (v, e)`.

Notes:

*  It's good if the intervals `e` from test vectors cover the whole `[0, 10]` interval.

   In other words, `union(abstract_test_vector[j].e)` should be closed to `[0, 10]` where `j = 1..L`.
*  It's good if values `e` are provided by experts in the domain.

## Verification procedure

An implementation of a rating must pass all tests defined by test vectors.

Let's say an implementation of a rating is defined as function `rating(v)`.
The function takes a vector `v` which contains values of features, and returns a rating score.

Then, the following verification procedure can be applied to make sure that the function `rating(v)` behaves as expected:

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

## Implementation

The verification procedure defined above is implemented in
[com.sap.sgs.phosphor.fosstars.model.qa](../src/main/java/com/sap/sgs/phosphor/fosstars/model/qa) package.

---

Next: [Tuning](tuning.md)
