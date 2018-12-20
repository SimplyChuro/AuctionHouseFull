import Route from '@ember/routing/route';
import { hash } from 'rsvp';

export default Route.extend({
  model(){
    return hash({
      products: this.store.query('product', { name: '', category: 0, featured: '', status: 'all', rating: '' })
    })
  }
});
