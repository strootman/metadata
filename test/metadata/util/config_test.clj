(ns metadata.util.config-test
  (:use [clojure.test])
  (:require [metadata.util.config :as config]))

(deftest test-config-defaults
  (testing "default configuration settings"
    (require 'metadata.util.config :reload)
    (config/load-config-from-file "conf/test/empty.properties")
    (is (= (config/listen-port) 60000))
    (is (= (config/environment-name) "docker-compose"))
    (is (= (config/db-driver-class) "org.postgresql.Driver"))
    (is (= (config/db-subprotocol) "postgresql"))
    (is (= (config/db-host) "dedb"))
    (is (= (config/db-port) "5432"))
    (is (= (config/db-name) "metadata"))
    (is (= (config/db-user) "de"))
    (is (= (config/db-password) "notprod"))
    (is (= (config/amqp-host) "rabbit"))
    (is (= (config/amqp-port) 5672))
    (is (= (config/amqp-user) "guest"))
    (is (= (config/amqp-pass) "guest"))
    (is (= (config/amqp-exchange) "de"))))