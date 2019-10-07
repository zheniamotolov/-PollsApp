import React, {Component} from 'react';
import {editUserName, getUserProfile} from "../../util/APIUtils";
import './UserInfo.css';
import {Button, Form, Input} from "antd";
import NotFound from "../../common/NotFound";
import ServerError from "../../common/ServerError";

import LoadingIndicator from '../../common/LoadingIndicator';

class UserInfo extends Component {
    render() {
        const AntWrapperUserInfoForm = Form.create()(UserInfoForm);
        return (

            <AntWrapperUserInfoForm username={this.props.match.params.username}/>
        )
    }
}

class UserInfoForm extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: null,
            isLoading: false
        };
        this.loadUserProfile = this.loadUserProfile.bind(this)
        this.handleSubmit = this.handleSubmit.bind(this)
    }

    loadUserProfile(username) {
        this.setState({
            isLoading: true
        });

        getUserProfile(username)
            .then(response => {
                this.setState({
                    user: response,
                    isLoading: false
                });
            }).catch(error => {

            if (error.status === 404) {
                this.setState({
                    notFound: true,
                    isLoading: false
                });
            } else {

                this.setState({
                    serverError: true,
                    isLoading: false
                });
            }
        });
    }

    handleSubmit(event) {
        event.preventDefault();
        this.props.form.validateFields((err, values) => {
            if (!err) {
                console.log(values);
                editUserName(this.props.username, values)
                    .then(response => {
                        window.location.reload();
                    })
            }
        });
    }

    componentDidMount() {
        const username = this.props.username;
        this.loadUserProfile(username);
    }

    // componentDidUpdate(nextProps) {
    //     if (this.props.match.params.username !== nextProps.match.params.username) {
    //         this.loadUserProfile(nextProps.match.params.username);
    //     }
    // }

    render() {
        const {getFieldDecorator} = this.props.form;
        if (this.state.isLoading) {
            return <LoadingIndicator/>;
        }

        if (this.state.notFound) {
            return <NotFound/>;
        }

        if (this.state.serverError) {
            return <ServerError/>;
        }
        return (
            <div className="userInfo">
                {
                    this.state.user ? (
                        <div className="edit-container">
                            <Form className="edit-form" onSubmit={this.handleSubmit}>
                                <Form.Item>
                                    {getFieldDecorator('name', {
                                        rules: [{required: true, message: 'Please new  name!'}],
                                    })(
                                        <Input
                                            placeholder={this.state.user.name}
                                            style={{marginTop: 50, marginBottom: 20}}/>
                                    )}
                                </Form.Item>
                                <Form.Item>
                                    <Button type="primary" htmlType="submit">Change</Button>
                                    <Button className="back-button" type="danger" href={"/"}>Cancel</Button>
                                </Form.Item>
                            </Form>
                        </div>
                    ) : null
                }
            </div>
        )

    }
}

export default UserInfo