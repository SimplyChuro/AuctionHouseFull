import Component from '@ember/component';
import ENV from 'auction-house/config/environment';
import { later } from '@ember/runloop';
import $ from 'jquery';
import { getOwner } from '@ember/application';

export default Component.extend({
  actions: {
    logout: function(){
      $.ajax({
        url: 'http://localhost:9000/api/v1/logout',
        type: 'POST',
        headers: { 
          'X-AUTH-TOKEN': ENV.USER_TOKEN
        }
      });

      ENV.USER_TOKEN = '';
      getOwner(this).lookup('router:main').transitionTo('home');
      
      later(this, function() {
        window.location.reload(true);
      }, 150);
    }
  }
});
