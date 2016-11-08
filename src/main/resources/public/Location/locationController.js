app.controller('locationController', ['$scope', '$state', '$http', 'loc',
  'reqs', '$uibModal', 'notifyDlg', '$q',
  function(scope, state, $http, loc, reqs, $uibM, nDlg, $q) {
    scope.reqs = reqs;
    scope.loc = loc;
    files = undefined;
    scope.getReqFile = function(req) {
      $http.get("api/reqs/" + req.id + "/file", {
          responseType: "arraybuffer"
        })
        .then(function(response) {
          var file = new Blob([response.data]);
          saveAs(file, req.fileName);
        });
    };

    scope.postReq = function() {
      $uibM.open({
          templateUrl: "Request/postReq.modal.html",
          scope: scope
        }).result
        .then(function(result) {
          return $http({
            method: "POST",
            url: "/api/locs/" + loc.id + "/reqs",
            headers: {
              'Content-Type': undefined
            },
            data: {
              ownerId: scope.user.id,
              description: result.description,
              file: files[0]
            },
            transformRequest: function(data, headersGetter) {
              var formData = new FormData();
              angular.forEach(data, function(value, key) {
                formData.append(key, value);
              });
              var headers = headersGetter();
              return formData;
            }
          });
        })
        .then(function() {
          return nDlg.show(scope, "Request Created Successfully",
            "Success");
        }).then(function() {
          state.reload();
        }).catch(function() {
          nDlg.show(scope, "Request Creation Failed", "Error");
        });
    };

    scope.deleteReq = function(req) {
      nDlg.show(scope, "Request Created Successfully", "Success", [
          "Confirm", "Cancel"
        ])
        .then(function(btn) {
          if (btn == "Confirm") {
            $http.delete("/api/reqs/" + req.id);
          } else {
            return $q.reject("Canceled");
          }
        })
        .then(function() {
          return nDlg.show(scope, "Request Deleted Successfully",
            "Success");
        })
        .then(function() {
          state.reload();
        })
        .catch(function(reason) {
          if (reason != "Canceled") {
            nDlg.show(scope, "Request Deletion Failed", "Error");
          }
        });
    };

    scope.storeFile = function(formFiles) {
      files = formFiles;
    };
  }
]);
