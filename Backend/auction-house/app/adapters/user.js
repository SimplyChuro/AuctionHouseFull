import ApplicationAdapter from './application';

export default ApplicationAdapter.extend({
	pathForType(){
		return 'users';
	},
	// Not finished
	// handleResponse(status, headers, body) {
	// 	if (status === 400) {
	// 		return '';
	// 	} else {
	// 		return this._super(status, headers, body);
	// 	}
	// }
});
