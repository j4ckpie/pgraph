# pgraph

## About
`pgraph` is a program designed to partition graphs described in the CSR-RG format into `k` parts. It aims to ensure that the difference in subgraph sizes doesn't exceed a specified `x%` tolerance, all while minimizing the number of edges that cross partition boundaries. The tool achieves this through a sophisticated heuristic that combines simulated annealing, local refinement, and a multi-start approach. Additionally, `pgraph` includes a user interface to visualize the partitioned graphs.

## Project Structure
```
repository/
├── model/          # Models
├── util/           # Utilities
├── view/           # UI related components
├── Main.java       # Main
└── README.md       # Project documentation
```
## More
`pgraph` is relies heavily on GUI. If you want to know more about how it works, please visit the [`dgraph` repo](https://github.com/j4ckpie/dgraph).

## Authors
Developed by Jakub Pietrala & Bartosz Starzyński.
