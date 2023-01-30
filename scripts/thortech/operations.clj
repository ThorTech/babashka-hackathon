(ns thortech.operations
  (:require [thortech.core.lambda :as lambda]))

(defn create-eon-system
  [request]
  (lambda/invoke-lambda
    "eas-dev-operations"
    {:task "create-eon-system"
     :args {:environment "20221108"}}))

(defn -main [& args]
  (println (create-eon-system {:test :wow})))