import Route from '@ember/routing/route';
import { hash } from 'rsvp';

export default Route.extend({
  model() {
    return hash({
      categoryList: this.store.findAll('category', { reload: true })
    })
  },
 
  actions: { 
    willTransition: function() {
      this.controllerFor('account/admin/new-product').send('clearFields');
    }
  }
});