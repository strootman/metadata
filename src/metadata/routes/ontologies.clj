(ns metadata.routes.ontologies
  (:use [common-swagger-api.schema]
        [metadata.routes.domain.common]
        [metadata.routes.domain.ontologies]
        [ring.util.http-response :only [ok]])
  (:require [metadata.services.ontology :as service]))

(defroutes* ontologies
  (context* "/ontologies" []
    :tags ["ontologies"]

    (POST* "/" []
           :query [{:keys [user]} StandardUserQueryParams]
           :multipart-params [ontology-xml :- String]
           :middlewares [service/wrap-multipart-xml-parser]
           :return OntologyDetails
           :summary "Save an Ontology"
           :description "Saves an Ontology XML document in the database."
           (ok (service/save-ontology-xml user ontology-xml)))

    (PUT* "/:ontology-version/:root-iri" []
          :path-params [ontology-version :- OntologyVersionParam
                        root-iri :- OntologyHierarchyRootParam]
          :query [{:keys [user]} StandardUserQueryParams]
          :return OntologyHierarchy
          :summary "Save an Ontology Hierarchy"
          :description
          "Save an Ontology Hierarchy, parsed from the Ontology XML stored with the given
           `ontology-version`, rooted at the given `root-iri`."
          (ok (service/save-hierarchy user ontology-version root-iri)))

    (GET* "/:ontology-version/:root-iri" []
          :path-params [ontology-version :- OntologyVersionParam
                        root-iri :- OntologyHierarchyRootParam]
          :query [{:keys [user]} StandardUserQueryParams]
          :return OntologyHierarchy
          :summary "Get an Ontology Hierarchy"
          :description "Gets an Ontology Hierarchy rooted at the given `root-iri`."
          (ok (service/get-hierarchy ontology-version root-iri)))

    (POST* "/:ontology-version/:root-iri/filter" []
           :path-params [ontology-version :- OntologyVersionParam
                         root-iri :- OntologyHierarchyRootParam]
           :query [{:keys [user]} StandardUserQueryParams]
           :body [{:keys [target-types target-ids]} OntologyHierarchyFilterRequest]
           :return OntologyHierarchy
           :summary "Filter an Ontology Hierarchy"
           :description
           "Filters an Ontology Hierarchy, rooted at the given `root-iri`, returning only the
            hierarchy's leaf-classes that are associated with the given targets."
           (ok (service/filter-hierarchy ontology-version root-iri target-types target-ids)))

    (POST* "/:ontology-version/:root-iri/filter-unclassified" []
           :path-params [ontology-version :- OntologyVersionParam
                         root-iri :- OntologyHierarchyRootParam]
           :query [{:keys [user]} StandardUserQueryParams]
           :body [{:keys [target-types target-ids]} OntologyHierarchyFilterRequest]
           :return TargetIDList
           :summary "Filter Unclassified Targets"
           :description
           "Filters the given target IDs by returning a list of any that are not associated with any
            Ontology classes of the hierarchy rooted at the given `root-iri`."
           (ok (service/filter-unclassified-targets ontology-version root-iri target-types target-ids)))))