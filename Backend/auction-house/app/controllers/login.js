import Controller from '@ember/controller';
import ENV from 'auction-house/config/environment';
import Cookies from 'ember-cli-js-cookie';
import swal from 'sweetalert';

export default Controller.extend({
  session: Ember.inject.service(),
  loginHasError: false,
  loginErrorMessage: 'Incorrect username or password',

  actions: {
    login: function() {
      var _this = this;
      $.ajax({
        url: ENV.HOST_URL+'/api/v1/login',
        type: 'POST',
        data: JSON.stringify({
          email: this.get('emailAddress'),
          password: this.get('password')
        }),
        contentType: 'application/json;charset=utf-8',
        dataType: 'json',
        success: function(data){
          Cookies.set('auth-token', data[0].authToken);
          Cookies.set('user-id', data[1].userID);
          _this.set('session.authToken', data[0].authToken);
          _this.set('session.userID', data[1].userID);
        }
      }).then(function(data){
        _this.set('loginHasError', false);
        _this.transitionToRoute('home');
      }).catch(function(data){
        _this.set('loginHasError', true);
      });
    },

    clearFields: function() {
      this.set('emailAddress', ''); 
      this.set('password', '');
      this.set('loginHasError', false);
    }
  }
});