'use strict';

angular
    .module('app.userView')
    .controller('UserViewController', UserViewController);

function UserViewController() {
    var vm = this;

    vm.userName = 'Name';

    console.log('init Ctrl with ' + vm.userName);
}