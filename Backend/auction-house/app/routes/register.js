import Route from '@ember/routing/route';
import { hash } from 'rsvp';

export default Route.extend({
  model(){
    return hash({
      user: this.store.createRecord('user', { reload: true })
    })
  },
  actions: {
    willTransition: function() {
      this.controllerFor('register').send('clearFields');
    }
  }
});
