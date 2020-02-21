/**
 * <p>The classes in this package have been originally generated
 * from the following CVE entry in NVD.</p>
 * <pre>
 *   {
 *     "cve" : {
 *       "data_type" : "CVE",
 *       "data_format" : "MITRE",
 *       "data_version" : "4.0",
 *       "CVE_data_meta" : {
 *         "ID" : "CVE-1999-0001",
 *         "ASSIGNER" : "cve@mitre.org"
 *       },
 *       "affects" : {
 *         "vendor" : {
 *           "vendor_data" : [ {
 *             "vendor_name" : "bsdi",
 *             "product" : {
 *               "product_data" : [ {
 *                 "product_name" : "bsd_os",
 *                 "version" : {
 *                   "version_data" : [ {
 *                     "version_value" : "3.1",
 *                     "version_affected" : "="
 *                   } ]
 *                 }
 *               } ]
 *             }
 *           }, {
 *             "vendor_name" : "freebsd",
 *             "product" : {
 *               "product_data" : [ {
 *                 "product_name" : "freebsd",
 *                 "version" : {
 *                   "version_data" : [ {
 *                     "version_value" : "1.0",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "1.1",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "1.1.5.1",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "1.2",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "2.0",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "2.0.1",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "2.0.5",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "2.1.5",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "2.1.6",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "2.1.6.1",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "2.1.7",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "2.1.7.1",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "2.2",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "2.2.2",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "2.2.3",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "2.2.4",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "2.2.5",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "2.2.6",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "2.2.8",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "3.0",
 *                     "version_affected" : "="
 *                   } ]
 *                 }
 *               } ]
 *             }
 *           }, {
 *             "vendor_name" : "openbsd",
 *             "product" : {
 *               "product_data" : [ {
 *                 "product_name" : "openbsd",
 *                 "version" : {
 *                   "version_data" : [ {
 *                     "version_value" : "2.3",
 *                     "version_affected" : "="
 *                   }, {
 *                     "version_value" : "2.4",
 *                     "version_affected" : "="
 *                   } ]
 *                 }
 *               } ]
 *             }
 *           } ]
 *         }
 *       },
 *       "problemtype" : {
 *         "problemtype_data" : [ {
 *           "description" : [ {
 *             "lang" : "en",
 *             "value" : "CWE-20"
 *           } ]
 *         } ]
 *       },
 *       "references" : {
 *         "reference_data" : [ {
 *           "url" : "http://www.openbsd.org/errata23.html#tcpfix",
 *           "name" : "http://www.openbsd.org/errata23.html#tcpfix",
 *           "refsource" : "CONFIRM",
 *           "tags" : [ ]
 *         }, {
 *           "url" : "http://www.osvdb.org/5707",
 *           "name" : "5707",
 *           "refsource" : "OSVDB",
 *           "tags" : [ ]
 *         } ]
 *       },
 *       "description" : {
 *         "description_data" : [ {
 *           "lang" : "en",
 *           "value" : "ip_input.c in BSD-derived TCP/IP implementations allows remote attackers to cause a denial of service (crash or hang) via crafted packets."
 *         } ]
 *       }
 *     },
 *     "configurations" : {
 *       "CVE_data_version" : "4.0",
 *       "nodes" : [ {
 *         "operator" : "OR",
 *         "cpe_match" : [ {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:bsdi:bsd_os:3.1:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:1.0:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:1.1:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:1.1.5.1:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:1.2:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:2.0:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:2.0.1:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:2.0.5:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:2.1.5:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:2.1.6:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:2.1.6.1:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:2.1.7:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:2.1.7.1:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:2.2:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:2.2.2:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:2.2.3:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:2.2.4:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:2.2.5:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:2.2.6:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:2.2.8:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:freebsd:freebsd:3.0:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:openbsd:openbsd:2.3:*:*:*:*:*:*:*"
 *         }, {
 *           "vulnerable" : true,
 *           "cpe23Uri" : "cpe:2.3:o:openbsd:openbsd:2.4:*:*:*:*:*:*:*"
 *         } ]
 *       } ]
 *     },
 *     "impact" : {
 *       "baseMetricV2" : {
 *         "cvssV2" : {
 *           "version" : "2.0",
 *           "vectorString" : "AV:N/AC:L/Au:N/C:N/I:N/A:P",
 *           "accessVector" : "NETWORK",
 *           "accessComplexity" : "LOW",
 *           "authentication" : "NONE",
 *           "confidentialityImpact" : "NONE",
 *           "integrityImpact" : "NONE",
 *           "availabilityImpact" : "PARTIAL",
 *           "baseScore" : 5.0
 *         },
 *         "severity" : "MEDIUM",
 *         "exploitabilityScore" : 10.0,
 *         "impactScore" : 2.9,
 *         "obtainAllPrivilege" : false,
 *         "obtainUserPrivilege" : false,
 *         "obtainOtherPrivilege" : false,
 *         "userInteractionRequired" : false
 *       }
 *     },
 *     "publishedDate" : "1999-12-30T05:00Z",
 *     "lastModifiedDate" : "2010-12-16T05:00Z"
 *   }
 * </pre>
 * <p>Then, the classes were updated to follow the code style guideline.</p>
 * <p>
 *   <i>jsonschema2pojo</i> has been used to generate the classes:
 *   <ul>
 *     <li><a href="http://www.jsonschema2pojo.org">http://www.jsonschema2pojo.org</a></li>
 *     <li>
 *       <a href="https://github.com/joelittlejohn/jsonschema2pojo/wiki/Getting-Started">
 *         Getting started
 *       </a>
 *     </li>
 *   </ul>
 * </p>
 */
package com.sap.sgs.phosphor.fosstars.nvd.data;