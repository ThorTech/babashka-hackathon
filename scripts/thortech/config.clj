(ns thortech.config
  (:require [cheshire.core :as json]
            [clojure.java.io :as io]
            [clojure.tools.cli :refer [parse-opts]]
            [com.grzm.awyeah.client.api :as aws]))

(def cli-options
  ;; An option with a required argument
  [["-e" "--env ENVIRONMENT" "Environment name, e.g. 20221108"
    ;; :default ""
    ;; :parse-fn #(Integer/parseInt %)
    ;; :validate [#(< 0 % 0x10000) "Must be a number between 0 and 65536"]
    ]
   ["-t" "--token TOKEN" "Token name, e.g. LegacyConfig"
    :default "LegacyConfig"]
   ["-h" "--help"]])

(defn invoke-lambda [client env token]
  (let [payload (io/input-stream (.getBytes (json/encode {:requestType "Query"
                                                          :compressResults false
                                                          :environmentName env
                                                          :tokenNames [token]})))
        out (aws/invoke client {:op :Invoke
                                :request
                                {:FunctionName "ConfigurationKeyRequests"
                                 :Payload payload}})]
    (println ::out (keys out))
    (with-open [r (io/reader (:Payload out))] 
      (doall (-> r (json/parse-stream true) :Values)))))

(defn exit
  "Helper to wrap System/exit"
  [status msg]
  (println msg)
  (System/exit status))

(defn -main
  [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)]
    (cond (:help options)
          (exit 0 summary)

          (empty? errors)
          (let [client (aws/client {:api :lambda})]
            (invoke-lambda client
                           (:env options)
                           (:token options))))))