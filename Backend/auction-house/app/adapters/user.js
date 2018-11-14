import ApplicationAdapter from './application';

export default ApplicationAdapter.extend({
	pathForType(){
		return 'users';
	}
	// handleResponse(status, headers, body) {
	// 	if (status === 400 && status === 401) {
	// 		return UnauthorizedError();
	// 	} else {
	// 		return this._super(status, headers, body);
	// 	}
	// }
});
