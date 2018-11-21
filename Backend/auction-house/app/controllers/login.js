import Controller from '@ember/controller';
import ENV from 'auction-house/config/environment';
import Cookies from 'ember-cli-js-cookie';
import { later } from '@ember/runloop';

export default Controller.extend({
  session: Ember.inject.service(),
  actions: {
    login: function() {
      this.get('session').login(this.get('emailAddress'), this.get('password'));
      this.transitionToRoute('home');
    }
  }
});