import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';
import { hash } from 'rsvp';

export default Route.extend({
  session: service(),

  model(){
    return hash({
      user: this.store.findRecord('user', this.get('session').userID, { reload: true }),
    })
  },
});
