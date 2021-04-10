# C++给定一个值中求数组元素最小差值

```cpp
#include <iostream>
#include <vector>
#include <climits>
#include <cstdlib>

#include <boost/assign/list_of.hpp>
#include <boost/foreach.hpp>

int main() {
  std::vector<int> chanVector = boost::assign::list_of(900)(1000)(1100);
  int chan = 1101;

  int min_diff_index = 0;
  int min_diff_val = INT_MAX;
  for (size_t i = 0; i < chanVector.size(); ++i) {
    if (chan == chanVector.at(i)) {
      min_diff_index = i;
      break;
    } else {
      int diff_tmp = abs(chan - chanVector.at(i));
      if (diff_tmp < min_diff_val) {
        min_diff_val = diff_tmp;
        min_diff_index = i;
      }
    }
  }

  std::cout << "min_diff_index = " << min_diff_index << std::endl;

  return 0;
}
```
