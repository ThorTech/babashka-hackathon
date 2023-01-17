#!/usr/bin/env bb

(require '[clojure.java.shell :refer [sh]]
         '[cheshire.core :as json])

(def now (java.time.ZonedDateTime/now))
(def LA-timezone (java.time.ZoneId/of "America/Los_Angeles"))
(def LA-time (.withZoneSameInstant now LA-timezone))
(def pattern (java.time.format.DateTimeFormatter/ofPattern "HH:mm"))
(println (.format LA-time pattern))

(defn babashka-latest-version []
  (-> (sh "curl" "https://api.github.com/repos/babashka/babashka/tags")
      :out
      (json/parse-string true)
      first
      :name))

(println (babashka-latest-version))
