import React, {Component} from 'react';
import {castVote, getAllPolls, getUserCreatedPolls, getUserVotedPolls} from '../util/APIUtils';
import Poll from './Poll';
import LoadingIndicator from '../common/LoadingIndicator';
import { notification, Pagination} from 'antd';
import {API_BASE_URL, POLL_LIST_PAGE_SIZE} from '../constants';
import './PollList.css';
import SearchPoll from "./SearchPoll";

class PollList extends Component {
    _isMounted = false;

    constructor(props) {
        super(props);

        this.state = {
            query: '',
            polls: [],
            currentPage: 0,
            size: 0,
            totalElements: 0,
            totalPages: 0,
            last: true,
            currentVotes: [],
            isLoading: false
        };

        this.loadPollList = this.loadPollList.bind(this);
        this.handleSearchChange = this.handleSearchChange.bind(this)
        this.handlePageChange = this.handlePageChange.bind(this)
    }

    loadPollList(page = 0, size = POLL_LIST_PAGE_SIZE, query = '') {
        let promise;
        console.log(API_BASE_URL)
        if (this.props.username) {
            if (this.props.type === 'USER_CREATED_POLLS') {
                promise = getUserCreatedPolls(this.props.username, page, size,query);
            } else if (this.props.type === 'USER_VOTED_POLLS') {
                promise = getUserVotedPolls(this.props.username, page, size,query);
            }
        } else {
            promise = getAllPolls(page, size, query);
        }

        if (!promise) {
            return;
        }

        this.setState({
            isLoading: true
        });

         promise
            .then(response => {
                if (this._isMounted) {
                    this.setState({
                        polls: response.content,
                        currentPage: response.page,
                        size: response.size,
                        totalElements: response.totalElements,
                        totalPages: response.totalPages,
                        last: response.last,
                        currentVotes: Array(response.content.length).fill(null),
                        isLoading: false
                    })
                }
            }).catch(error => {
            this.setState({
                isLoading: false,
                message: error.message
            })
        });

    }

    componentDidMount() {
        this._isMounted = true;
        this.loadPollList();
    }

    componentWillUnmount() {
        this._isMounted = false;
    }

    componentDidUpdate(nextProps) {
        if (this.props.isAuthenticated !== nextProps.isAuthenticated) {
            // Reset State
            this.setState({
                polls: [],
                currentPage: 0,
                size: 0,
                totalElements: 0,
                totalPages: 0,
                last: true,
                currentVotes: [],
                isLoading: false
            });
            this.loadPollList();
        }
    }

    handleVoteChange(event, pollIndex) {
        const currentVotes = this.state.currentVotes.slice();
        currentVotes[pollIndex] = event.target.value;

        this.setState({
            currentVotes: currentVotes
        });
    }

    handleSearchChange(query) {
            this.setState({query: query, isLoading: true, message: ''}, () => {
                this.loadPollList(0, POLL_LIST_PAGE_SIZE, query) // important moment that we call fetchSearchResults inside
                // callback function of setState, thaw because setState is asynchronus we sure that fetchSearchResults would
                // be called after setState
            })

    }

    handlePageChange(pageNo) {
        this.setState({currentPage: pageNo - 1},
            this.loadPollList(pageNo - 1, POLL_LIST_PAGE_SIZE, this.state.query))
    }

    handleVoteSubmit(event, pollIndex) {
        event.preventDefault();
        if (!this.props.isAuthenticated) {
            this.props.history.push("/login");
            notification.info({
                message: 'Polling App',
                description: "Please login to vote.",
            });
            return;
        }

        const poll = this.state.polls[pollIndex];
        const selectedChoice = this.state.currentVotes[pollIndex];

        const voteData = {
            pollId: poll.id,
            choiceId: selectedChoice
        };

        castVote(voteData)
            .then(response => {
                const polls = this.state.polls.slice();
                polls[pollIndex] = response;
                if (this._isMounted) {
                    this.setState({
                        polls: polls
                    });
                }
            }).catch(error => {
            if (error.status === 401) {
                this.props.handleLogout('/login', 'error', 'You have been logged out. Please login to vote');
            } else {
                notification.error({
                    message: 'Polling App',
                    description: error.message || 'Sorry! Something went wrong. Please try again!'
                });
            }
        });
    }

    render() {
        const pollViews = [];
        this.state.polls.forEach((poll, pollIndex) => {
            pollViews.push(<Poll
                key={poll.id}
                poll={poll}
                currentVote={this.state.currentVotes[pollIndex]}
                handleVoteChange={(event) => this.handleVoteChange(event, pollIndex)}
                handleVoteSubmit={(event) => this.handleVoteSubmit(event, pollIndex)}/>)
        });

        return (


            <div className="polls-container">
                <SearchPoll handleSearchChange={this.handleSearchChange}

                />

                {pollViews}
                {
                    !this.state.isLoading && this.state.polls.length === 0 ? (
                        <div className="no-polls-found">
                            <span>No Polls Found.</span>
                        </div>
                    ) : null
                }

                <Pagination
                    defaultCurrent={1}
                    defaultPageSize={POLL_LIST_PAGE_SIZE} //default size of page
                    onChange={this.handlePageChange}
                    total={this.state.totalElements} //total number of card data available
                />
                {
                    this.state.isLoading ?
                        <LoadingIndicator/> : null
                }
            </div>

        );
    }
}

export default PollList;