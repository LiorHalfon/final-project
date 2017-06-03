
<!DOCTYPE html>
<html>
<head>
  <meta charset="ISO-8859-1">
  <title>AJAX with Servlets using AngularJS</title>
  <script type="text/javascript" src="assets/js/libs/angular.min.js"></script>
  <script>
      var app = angular.module('myApp', []);
      app.controller("myCtrl",function($scope, $http) {
          $scope.getDataFromServer = function() {
              $http({
                  method : 'GET',
                  url : 'javaAngularJS'
              }).then(function mySuccess(response) {
                  $scope.person = response.data;
              }, function myError(response) {
              });
          };
      });
  </script>
</head>
<body>
<div ng-app="myApp">
  <div ng-controller="myCtrl">
    <button ng-click="getDataFromServer()">Fetch data from server</button>
    <p>First Name : {{person.firstName}}</p>
    <p>Last Name : {{person.lastName}}</p>
  </div>
</div>
</body>
</html>