(def-type Edge (-> Str (Set Str)))
(def-type Reachable (-> Str (Set Str)))
(def-type Cycle (Set Str))

(fact (Edge "a" {"b"}))
(fact (Edge "b" {"c"}))
(fact (Edge "c" {"a"}))

(rule (Reachable x {y}) ((Edge x {y})))
(rule (Reachable x {z}) ((Reachable x {y}) (Reachable y {z})))

(rule (Cycle {x}) ((Reachable x {x})))