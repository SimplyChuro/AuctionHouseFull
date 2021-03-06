import Component from '@ember/component';
import ENV from 'auction-house/config/environment';
import $ from 'jquery';
import Cookies from 'ember-cli-js-cookie';
import { inject as service } from '@ember/service';

export default Component.extend({
  session: service(),
  router: service(),
  store: service(),

  actions: {
    async logout(){
      var _this = this;

      Cookies.remove('auth-token');
      Cookies.remove('user-id');
      Cookies.remove('admin-checker');
      _this.get('router').transitionTo('index');
      _this.get('store').unloadAll('wishlist');
      _this.get('store').unloadAll('user');

      $.ajax({
        url: ENV.HOST_URL+'/api/v1/logout',
        type: 'POST',
        headers: {
          'X-AUTH-TOKEN': _this.get('session').authToken
        },
        contentType: 'application/text'
      }).then(function(){
        _this.set('session.authToken', null);
        _this.set('session.userID', null);
        _this.set('session.adminChecker', null);
      });
    },

    searchForItem: function(){
      this.get('router').transitionTo('shop.product-list', {queryParams: {name: this.get('searchQuery')}});
    }
  }
});
