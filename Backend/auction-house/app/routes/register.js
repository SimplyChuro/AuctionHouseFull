import Route from '@ember/routing/route';
import { hash } from 'rsvp';

export default Route.extend({
  actions: {
    willTransition: function() {
      this.controllerFor('register').send('clearFields');
    }
  }
});
