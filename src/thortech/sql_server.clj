#!/usr/bin/env bb

; See: https://github.com/babashka/babashka-sql-pods
(require '[babashka.pods :as pods])

(pods/load-pod 'org.babashka/mssql "0.1.1")

(require '[pod.babashka.mssql :as sql]
         ;'[clojure.edn :as edn]
         )

(def active-event-sql
  "SELECT ConfigEnvironment FROM Systems WHERE SelectedDuringLogin = 1;")

(def db {:dbtype   "mssql"
         :host     "sqlserver-lite.dev.electiontonight.com"
         :dbname   "ENSAdmin"
         :user     "sa"
         :password "PASSWORD"
         :port     1433})

;(def db (edn/read-string (slurp "/Users/wcallahan/git/babashka-hackathon/resources/db-connection.edn")))

(sql/execute! db [active-event-sql])

