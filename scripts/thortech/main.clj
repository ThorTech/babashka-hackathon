(ns thortech.main
  (:require [clojure.java.shell :refer [sh]]
            [cheshire.core :as json]
            [org.httpkit.client :as http]))



(def now (java.time.ZonedDateTime/now))
(def LA-timezone (java.time.ZoneId/of "America/Los_Angeles"))
(def LA-time (.withZoneSameInstant now LA-timezone))
(def pattern (java.time.format.DateTimeFormatter/ofPattern "HH:mm"))
(println (.format LA-time pattern))

(http/request {:url "http://www.cnn.com" :method :get}
              (fn [{:keys [opts status body headers error] :as resp}]
                (if error
                  (println "Error on" opts)
                  (println "Success on" opts))))

(defn babashka-latest-version []
  (-> (sh "curl" "https://api.github.com/repos/babashka/babashka/tags")
      :out
      (json/parse-string true)
      first
      :name))

(println (babashka-latest-version))
