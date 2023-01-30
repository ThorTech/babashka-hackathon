(ns thortech.clear-cache
  (:require [thortech.core.lambda :as lambda]
            [clojure.tools.cli :refer [parse-opts]]))

(defn clear-cache
  [environment is-election-day]
  (lambda/invoke-lambda
    "CacheRefresheas-cache-refreshmaster"
    {:Environment environment
     :IsElectionDay is-election-day
     :ShouldThrow false}))

(def cli-options
  ;; An option with a required argument
  [["-e" "--environment ENVIRONMENT" "Environment"]
   ["-d" "--eday" "Is this election day?" :default false]])

(defn -main
  [& args]
  (let [parsed (parse-opts args cli-options)
        options (:options parsed)]
    (cond (empty? (:errors parsed))
          (clear-cache (:environment options) (:eday options)))))
