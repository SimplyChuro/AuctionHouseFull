import Controller from '@ember/controller';
import ENV from 'auction-house/config/environment';
import $ from 'jquery';
import Cookies from 'ember-cli-js-cookie';
import { inject as service } from '@ember/service';

export default Controller.extend({
  session: service(),
  customSession: service(),
  loadingSlider: service(),

  loginHasError: false,
  loginErrorMessage: '',

   actions: {
    async login() {
      var _this = this;
      _this.get('loadingSlider').startLoading();

      await $.ajax({
        url: ENV.HOST_URL+'/api/v1/login',
        type: 'POST',
        data: JSON.stringify({
          email: this.get('emailAddress'),
          password: this.get('password')
        }),
        contentType: 'application/json;charset=utf-8',
        dataType: 'json',
        success: function(data) {
          if(_this.get('rememberMe') == true) {
            Cookies.set('auth-token', data.authToken);
            Cookies.set('user-id', data.userID);
            Cookies.set('admin-checker', data.adminChecker);
          } else {
            Cookies.set('auth-token', data.authToken , { expires: 0.1 });
            Cookies.set('user-id', data.userID , { expires: 0.1 });
            Cookies.set('admin-checker', data.adminChecker , { expires: 0.1 });
          }
          _this.set('customSession.authToken', data.authToken);
          _this.set('customSession.userID', data.userID);
          _this.set('customSession.adminChecker', data.adminChecker);
        },
        error: function (data) {
          var msg = $.parseJSON(data.responseText);
          _this.set('loginHasError', true);
          _this.set('loginErrorMessage', msg.error_message);
        }
      }).then(function() {
        _this.get('loadingSlider').endLoading();
        _this.set('loginHasError', false);
        _this.transitionToRoute('home');
      }).catch(function() {
        _this.get('loadingSlider').endLoading();
        _this.set('loginHasError', true);
      });
    },

    async loginFacebook() {
     var _this = this;

      _this.get('session').authenticate('authenticator:torii-facebook', 'facebook-oauth2').then(function() {
        _this.set('loginHasError', false);
        _this.transitionToRoute('home');
      });
    },

    async loginGmail() {
      var _this = this;

      _this.get('session').authenticate('authenticator:torii-google', 'google-oauth2').then(function() {
        _this.set('loginHasError', false);
        _this.transitionToRoute('home');
      });
    },

    clearFields: function() {
      this.set('emailAddress', ''); 
      this.set('password', '');
      this.set('rememberMe');
      this.set('loginHasError', false);
      this.set('loginErrorMessage', '');
    }
    
  }
});