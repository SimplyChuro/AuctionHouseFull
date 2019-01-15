import Controller from '@ember/controller';
import { inject as service } from '@ember/service';
import ENV from 'auction-house/config/environment';
import $ from 'jquery';
import Cookies from 'ember-cli-js-cookie';
import swal from 'sweetalert';

export default Controller.extend({
  session: service(),
  router: service(),

  actions: {

    async deactivateAccount() {
      var _this = this;

      $.ajax({
        url: ENV.HOST_URL+'/api/v1/deactivate',
        type: 'POST',
        headers: {
          'X-AUTH-TOKEN': _this.get('session').authToken
        },
        contentType: 'application/text'
      }).then(function(){
        Cookies.remove('auth-token');
        Cookies.remove('user-id');
        Cookies.remove('admin-checker');
        _this.transitionToRoute('home');
        _this.store.unloadAll('wishlist');
        _this.store.unloadAll('user');
        _this.set('session.authToken', null);
        _this.set('session.userID', null);
        _this.set('session.adminChecker', null);
        swal("Success!", "You have successfully deactivated your account!", "success");
      });

    },

    clearFields: function(){
      // this.set('currentCategory', null);
      
    }

  }

});
