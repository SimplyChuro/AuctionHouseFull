import Component from '@ember/component';
import ENV from 'auction-house/config/environment';
import $ from 'jquery';
import { getOwner } from '@ember/application';
import { later } from '@ember/runloop';

export default Component.extend({
  session: Ember.inject.service(),
  actions: {
    logout: function(){
      this.get('session').logout();
      getOwner(this).lookup('router:main').transitionTo('home');
    
    }
  }
});
