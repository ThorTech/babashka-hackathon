(ns thortech.cli-commands
  (:require [babashka.cli :as cli]))

(defn copy [m]
  (assoc m :fn :copy))

(defn delete [m]
  (assoc m :fn :delete))

(defn help [m]
  (assoc m :fn :help))

(def table
  [{:cmds ["copy"]   :fn copy   :args->opts [:file]}
   {:cmds ["delete"] :fn delete :args->opts [:file]}
   {:cmds []         :fn help}])

(defn -main [& args]
  (cli/dispatch table args {:coerce {:depth :long}}))