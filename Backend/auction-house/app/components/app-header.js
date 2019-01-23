import Component from '@ember/component';
import ENV from 'auction-house/config/environment';
import $ from 'jquery';
import Cookies from 'ember-cli-js-cookie';
import { inject as service } from '@ember/service';

export default Component.extend({
  customSession: service(),
  router: service(),
  store: service(),

  currentDate: null,

  actions: {
    async logout(){
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
        url: ENV.HOST_URL+'/api/v1/logout',
        type: 'POST',
        headers: {
          'X-AUTH-TOKEN': token
        },
        contentType: 'application/text'
      });
    },

    searchForItem: function() {
      this.get('router').transitionTo('shop.product-list', {queryParams: {name: this.get('searchQuery')}});
    }
  }
});
