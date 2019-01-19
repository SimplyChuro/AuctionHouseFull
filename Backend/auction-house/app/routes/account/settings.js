import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';
import { hash } from 'rsvp';

export default Route.extend({
  customSession: service(),

  model(){
    return hash({
      user: this.store.findRecord('user', this.get('customSession').getUserID(), { reload: true }),
    })
  },
});
