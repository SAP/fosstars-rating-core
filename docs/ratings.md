# Defining a rating for an open-source project

This page provides definitions for a feature, a score and a rating.
Then, it describes how a rating for an open-source project can be defined.

## Feature

Various data may be used to build a rating for an open-source project.
The data may be very diverse and may have different types.

Let's define a **feature** as a measurable characteristic of an open-source project.
A feature has a type and may have constraints.

Here are several examples of features, their types and constraints:

*  Number of commits last month (a non-negative integer)
*  CVSS scores for vulnerabilities (a float number from 0 to 10)
*  Date of latest release (date)
*  Supported by a company (boolean, yes or no)

## Score

A number of features may describe a particular property of an open-source project.

Let's define a **score** as a procedure which takes a number of features or other scores
and produces a float number in the interval `[0, 10]`. The procedure is also called **score function**.
The number is a score value which describes a particular property of an open-source project.

The higher a score is, the better a property is implemented in an open-source project.
A score can be also called a synthetic feature.

Here are several examples of scores:

*  Security awareness score describes how well maintainers and community of an open-source project care about security.
   The security awareness score may be based on user's opinions about the topic, presence of a security team, etc.
*  Unpatched vulnerabilities score describes how unpatched vulnerabilities impact security of an open-source project.
   The unpatched vulnerabilities score may be based on a total number of vulnerabilities,
   a total number of unpatched vulnerabilities, CVSS scores for the unpatched vulnerabilities, etc.
*  Security testing score describes how well security testing is being performed for an open-source project.
   The security testing score may be based on a history of security reviews, security tests,
   static code analysis, fuzzing, etc.
*  Project activity score describes how alive an open-source project is.
   The project activity score may be based on statistics from a code repository
   such as a number of commits last month, a number of contributors last month, etc.
*  Commitment score describes how well maintainers and community of an open-source project is committed
   to support the project. The commitment score may be based on a number of active maintainers,
   presence of a company which sponsors the project, etc.

## Rating

A number of properties of an open-source project may be combined to describe a more general property of the project.

Let's define a **rating** as a set of labels and a procedure which maps a score value to a one of the labels.
The labels describe a general property of an open-source project. A rating is based on a number of scores.

For example, a security rating for an open-source project may be based on the following scores:

*  Security awareness score
*  Unpatched vulnerabilities score
*  Security testing score
*  Project activity score
*  Commitment score

## Hierarchy of features, scores and ratings

Dependencies between features, scores and ratings may be described as a graph.

```
                            +----------+
                            |  rating  |
                            +--^---^---+
                               |   |
                       +-------+   +-------+
                       |                   |
              +--------+--+             +--+------+
              |  score 1  |     ...     | score M |
              +--^--^--^--+             +--^---^--+
                 |  |  |                   |   |
         +-------+  |  +------------+  +---+   +------+
         |          |               |  |              |
+--------+--+  +----+------+     +--+--+-----+     +--+--------+
| feature 1 |  | feature 2 | ... | feature i | ... | feature N |
+-----------+  +-----------+     +-----------+     +-----------+
```

The graph looks like a tree. In this graph, a rating is the root of the tree, scores are nodes, and features are leafs.
However, strictly speaking, the graph is not a tree because a feature can contribute to multiple scores,
so that the graph is going to have a loop.

## Defining a rating

The following steps describe how a rating can be built:

1.  Define a set of features `F = { f[1], f[2], ... , f[N] }`.
1.  Define a set of scores `S = { s[1], s[2], ... , s[M] }`.
1.  For each score `s[i]` where `i = 1..M`:
    1.  Assign a set of features `F_s[i]` which contains features from `F` which are used to build the score `s[i]`.

        By definition, each set `F_s[i]` is a subset of `F`. Note that the sets `F_s[i]` may overlap.
    2.  Define a score function `score[i]` which takes features from `F_s[i]` and return a score in the interval `[0, 10]`.

        In other words, `score[i]: F_s[i] -> [0, 10]`.
    3.  Assign a weight `w[i]` in the interval `(0, 1]`.
1.  Now, a rating score can be calculated by the following function `rating(v)`.

    The function takes a vector of feature values `f[i]` and returns a number:

    ```
    def rating(v) {
        scores = 0
        sum_of_weights = sum of w[i] for i=1..M
        for i in 1..M
            F_s_v = select F_s[i] values from v
            scores = scores + w[i] * score[i](F_s_v)
        return scores / sum_of_weights
    }
    ```

    In other words, the function `rating(v)` calculates a weighted average of scores `s[i]`.

    By definition, the `rating(v)` function always returns a number in the interval `[0, 10]`.
1.  Define a set of labels `L = { l[1], ... , l[K] }`.
1.  Define a function `label(r)` which maps a rating `r` to one of the labels from `L`.

    In other words, `label[r]: r -> l` where `r` belongs to the interval `[0, 10]` and `l` belongs to `L`.

The weight `w[i]` define how much a scores `s[i]` contributes to the overall rating.

In other words, the weight `w[i]` defines the importance of the score `s[i]`.

---

Next: [Example](example.md)
