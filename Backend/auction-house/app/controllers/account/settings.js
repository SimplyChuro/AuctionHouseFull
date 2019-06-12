import Controller from '@ember/controller';
import { inject as service } from '@ember/service';
import ENV from 'auction-house/config/environment';
import $ from 'jquery';
import Cookies from 'ember-cli-js-cookie';
import swal from 'sweetalert';

export default Controller.extend({
  customSession: service(),
  router: service(),

  mailNotificationCheckbox: null,
  pushNotificationCheckbox: null,
  textNotificationCheckbox: null,

  mailCheckboxObserver: function() {
    let user = this.get('model.user');
    if(this.get('mailNotificationCheckbox')) {
      if(!user.emailNotification) {
        user.set('emailNotification', true);
        user.save();
      }
    } else {
      if(user.emailNotification) {
        user.set('emailNotification', false);
        user.save();
      }
    }
  }.observes('mailNotificationCheckbox'),

  pushCheckboxObserver: function() {
    let user = this.get('model.user');
    if(this.get('pushNotificationCheckbox')) {
      Notification.requestPermission();
      if(!user.pushNotification) {
        user.set('pushNotification', true);
        user.save();
      }
    } else {
      if(user.pushNotification) {
        user.set('pushNotification', false);
        user.save();
      }
    }
  }.observes('pushNotificationCheckbox'),

  actions: {

    async deactivateAccount() {
      var _this = this;

      var token = _this.get('customSession').getAuthToken();

      Cookies.remove('auth-token');
      Cookies.remove('user-id');
      Cookies.remove('admin-checker');
      window.localStorage.clear();
      _this.get('router').transitionTo('index');
      _this.get('store').unloadAll('wishlist');
      _this.get('store').unloadAll('user');
      _this.set('customSession.authToken', null);
      _this.set('customSession.userID', null);
      _this.set('customSession.adminChecker', null);
      _this.set('currentDate', moment(new Date()));

      $.ajax({
        url: ENV.HOST_URL+'/api/v1/deactivate',
        type: 'POST',
        headers: {
          'X-AUTH-TOKEN': token
        },
        contentType: 'application/text'
      }).then(function() {
        swal("Success!", "You have successfully deactivated your account!", "success");
      });

    },

    clearFields: function() {
      
    }

  }

});
