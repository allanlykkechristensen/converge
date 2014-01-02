/*
 * Copyright (C) 2014 Converge Consulting
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * This package contains JPA entities used for managing authentication and
 * authorization. Security in Converge is based on Apache Shiro and consists of
 * the following objects:<br/>
 * <ul>
 * <li>{@link UserAccount} representing a user of Converge</li>
 * <li>{@link UserRole} representing a role that a {@link UserAccount} can be a
 * member of. {@linkplain UserRoles User roles} is used for identifying users of
 * a certain role in the system and as a convenient way of granting the same
 * permissions to a group of users.</li>
 * <li>{@link Permission} representing something that a {@link UserAccount} or
 * {@link UserRole} is authorized to access.</li>
 * </ul>
 */
package com.getconverge.converge.entities.security;
