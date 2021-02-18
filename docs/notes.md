# Notes

## How data can expire

The relevance of some data which are used to built ratings should decrease over time.

For example, let's say a security expert completed a security review for an open source project.
Then, for sure, we can rely on the results during certain period of time.
During this period of time the results of the review should affect the overall security rating the most.
After some time the results of the review may become a bit outdated,
so that it should affect the overall security rating a bit less.
However, after some more time, we should not count this information at all since they are expired.
In other words, the results of the review should affect the overall security rating only for limited period of time
when they may be considered relatively up-to-date.

Expiration of particular features may depend not only on age of provided data.
For the example above, the results of a security review may become outdated quite fast
if the project is under active development because the code gets updated quite often.
On the other hand, if we consider a project which is not actively updated,
then results of a security review may stay up-to-date much longer.

The data expiration process may be defined with a logistic function.
Parameters of the logistic function may vary for specific features.
